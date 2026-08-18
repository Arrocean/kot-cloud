# 项目开发规范

本文档定义 `kot-cloud` 的仓库级开发规范。[AGENTS.md](AGENTS.md) 是本仓库对开发者和自动化工具的权威约束，本文档与其保持一致。

## 架构规范

系统模块服务端采用 DDD，依赖方向必须严格保持为：

```text
adapter -> application -> domain <- infrastructure
```

- `adapter`：仅包含 Controller、请求/响应 DTO、Assembler、参数校验和传输层逻辑。
- `application`：通过 Command、Query、Handler 和 Facade 编排用例，负责事务边界，不承载领域规则。
- `domain`：包含聚合、值对象、Repository 接口和业务规则；不得依赖 Micronaut、adapter 或 infrastructure。
- `infrastructure`：实现 domain Repository 接口，包含 ORM Entity、持久化 Mapper 和外部集成。
- 应用服务只能依赖 domain 中定义的 Repository 接口，不得注入具体 infrastructure 实现。
- 跨模块调用必须使用 `*-api` 模块暴露的契约，不得直接依赖其他 server 模块内部类。

## Kotlin 代码规范

- 使用既有包根路径：`com.arrocean.dev`。
- 类型使用大驼峰，函数和属性使用小驼峰，常量使用全大写下划线命名。
- 使用 4 个空格缩进，并遵循仓库现有 Kotlin 格式。
- 优先使用不可变的 `val`；仅在框架要求或领域生命周期确实需要时使用 `var`。
- 保持函数聚焦；只有在消除实际复杂度或具备复用价值时才提取辅助方法。
- 对可选请求字段使用 Kotlin 可空类型和校验注解，不要使用魔法值或哨兵值。
- 注释仅用于说明非显而易见的决策、约束或算法，不要重复描述代码本身。

## 命名与目录

- Controller：`XxxControllerV1`。
- 请求和响应：`XxxRequest`、`XxxResponse`；同类 DTO 按现有模式集中在 `XxxModels.kt`。
- Assembler：`XxxAssembler`。
- Command、Query 与 Handler：`XxxCommand`、`XxxQuery`、`XxxCommandHandler`、`XxxQueryHandler`。
- 应用层 Facade/Service：`XxxAppService` 或 `XxxService`。
- Domain Repository 放在 `domain/<feature>/repository`；实现放在 `infrastructure/persistence/repositoryimpl`。
- ORM Entity 放在 `infrastructure/persistence/entity`，不得作为 API DTO 或领域模型复用。

## HTTP 与 API 规范

- Controller 保持轻量：校验输入、调用应用服务、转换返回结果。
- 普通 API 响应使用 `CommonResult<T>`；可预期的业务异常使用 `ServiceException` 和 `ErrorCode`。
- 保持 `ApiPrefix` 提供的版本化路由规范，包括 `/v{n}/admin-api` 和 `/v{n}/app-api`。
- 管理端受保护操作使用 `@RequirePermission`；菜单可见性不能代替服务端鉴权。
- 使用既有 Micronaut OpenAPI 注解维护公开接口和响应模型文档。
- 不得为了简化接口文档导入而改变运行时路由。

## RBAC 与租户约束

- `super_admin` 仅能由数据库运维人员维护，业务 API 不得创建、修改、分配、撤销或删除该角色。
- 公开管理员注册完成后自动绑定可编辑的 `system_admin` 角色。
- 未经明确失效机制，不得引入会延迟权限变更生效的 RBAC 缓存。当前实现直接查询持久化层，角色或权限修改应在下一次请求生效。
- `tenant_id` 仅作为模型和表结构准备存在；当前运行行为仍为单租户，未经明确架构决策不得自行加入租户过滤或隔离语义。

## 持久化与表结构

- PostgreSQL 是当前受支持的持久化运行时。
- Domain Repository 接口不得依赖 Micronaut Data 或 PostgreSQL 实现类型。
- 需要可复现初始化的表结构变更，应同步更新 `sql/kot_cloud_system.sql`。
- 该 SQL 脚本会删除并重建系统表，只能作为初始化脚本，不能当作存量环境的在线迁移方案。
- 新增系统或 RBAC 表结构时，为表、字段和约束补充有意义的 PostgreSQL 注释。

## 安全与配置

- 不得提交密码、带凭据的数据库 URL、Redis 凭据、JWT 密钥、access token、refresh token 或私钥。
- 部署相关配置通过环境变量提供，例如 `R2DBC_URL`、`R2DBC_USER`、`R2DBC_PASSWORD`、`REDIS_URI` 和 `JWT_SECRET`。
- 生产环境 `JWT_SECRET` 必须随机生成且不少于 32 字节；仓库中的默认值仅用于本地启动兜底。
- 密码处理必须使用 framework 中的 `PasswordEncoder` 抽象，不得在业务模块中自行实现哈希逻辑。
- 日志不得记录原始密码、Authorization 请求头、access token、refresh token、session ID 或含凭据的连接字符串。

## OpenAPI 与 Apifox

- 原始 OpenAPI 文档使用 OpenAPI 3.0.1 生成。
- 运行 `:kot-module-system:kot-module-system-server:generateApifoxOpenApi` 生成 Apifox 专用 YAML。
- `kot-cloud-apifox.yml` 必须按 OpenAPI 3.0 YAML 导入，不要转换为 OpenAPI 3.1。
- 修改 Apifox 项目内容前，必须先读取项目 `7595187` 中 ID 为 `443783480` 的既有标准接口文档。

## 构建与验证

- 使用仓库内的 Gradle Wrapper。
- 当前工作区设置 `GRADLE_USER_HOME=D:\IDEA\.gradle`，不得使用 C 盘默认 Gradle 缓存。
- 未经明确请求或已验证的兼容性问题，不得调整依赖版本。
- 代码修改后执行最小范围的编译、测试或构建任务；仅文档变更使用 diff 检查即可。
- `ksp.incremental=false` 为有意设置，用于规避 Micronaut OpenAPI 聚合生成在增量 PSI 状态下的失败问题。

## Git 规范

- 工作区存在其他改动时必须保留，不得重置、checkout 或回退非本人改动。
- 使用非交互式 Git 命令。
- 未经明确要求，不得提交、修订提交、推送或创建 Pull Request。
- 提交时使用以下规范格式：

  ```text
  [type](module): summary

  - Change summary one
  - Change summary two
  ```

- 常用类型包括 `feat`、`fix`、`docs`、`refactor`、`test`、`build` 和 `chore`。
