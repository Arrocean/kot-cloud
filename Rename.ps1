# PowerShell 脚本，用于批量替换文本文件中的包名，并可选地重命名目录结构。
# requires -PSEdition Core
# requires -Version 7.0

[CmdletBinding()]
param(
    [string]$Root = $PSScriptRoot,
    [switch]$Apply,
    [switch]$RenameDirectories
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$OldPackage = "com.arrocean.dev"
$NewPackage = "com.xxx.dev"

$TextFilePatterns = @(
    "*.kt", "*.kts", "*.java",
    "*.properties", "*.yml", "*.yaml",
    "*.xml", "*.gradle", "*.md",
    "*.txt", "*.sql"
)

function Get-TextFiles {
    param([string]$BaseDir)

    @(
        foreach ($pattern in $TextFilePatterns) {
            Get-ChildItem -LiteralPath $BaseDir -Recurse -File -Filter $pattern -ErrorAction SilentlyContinue
        }
    ) | Sort-Object -Property FullName -Unique
}

function Replace-InFiles {
    param(
        [string]$BaseDir,
        [bool]$DoApply
    )

    $files = Get-TextFiles -BaseDir $BaseDir
    $changedFiles = @()

    foreach ($file in $files) {
        $content = Get-Content -LiteralPath $file.FullName -Raw -Encoding UTF8
        if ($null -eq $content) {
            continue
        }

        $newContent = $content.Replace($OldPackage, $NewPackage)

        if ($newContent -ne $content) {
            $changedFiles += $file.FullName

            if ($DoApply) {
                Set-Content -LiteralPath $file.FullName -Value $newContent -Encoding UTF8
            }
        }
    }

    return $changedFiles
}

function Rename-PackageDirectories {
    param([string]$BaseDir)

    $targets = Get-ChildItem -Path $BaseDir -Recurse -Directory -ErrorAction SilentlyContinue |
            Where-Object {
                $_.FullName -match [regex]::Escape((Join-Path (Join-Path "com" "arrocean") "dev"))
            } |
            Sort-Object { $_.FullName.Length } -Descending

    foreach ($dir in $targets) {
        $fullPath = $dir.FullName

        $comDir = Split-Path (Split-Path $fullPath -Parent) -Parent
        $expectedOld = Join-Path $comDir (Join-Path "arrocean" "dev")
        $newwhitespriteDir = Join-Path $comDir "xxx"
        $newDevDir = Join-Path $newwhitespriteDir "dev"

        if ($fullPath -ieq $expectedOld) {
            if (-not (Test-Path -LiteralPath $newwhitespriteDir)) {
                New-Item -ItemType Directory -Path $newwhitespriteDir | Out-Null
            }

            if (-not (Test-Path -LiteralPath $newDevDir)) {
                Move-Item -LiteralPath $fullPath -Destination $newDevDir
            } else {
                Write-Warning "目标目录已存在，跳过目录移动：$fullPath -> $newDevDir"
            }

            $oldwhitespriteDir = Join-Path $comDir "arrocean"
            if ((Test-Path -LiteralPath $oldwhitespriteDir) -and
                    -not (Get-ChildItem -LiteralPath $oldwhitespriteDir -Force -ErrorAction SilentlyContinue)) {
                Remove-Item -LiteralPath $oldwhitespriteDir -Force
            }
        }
    }
}

Write-Host "Root: $Root"
Write-Host "Replace package: $OldPackage -> $NewPackage"
Write-Host ""

$preview = @(Replace-InFiles -BaseDir $Root -DoApply:$false)

if (-not $preview -or $preview.Count -eq 0) {
    Write-Host "未发现需要替换的文本文件。"
} else {
    Write-Host "将受影响的文件："
    $preview | ForEach-Object { Write-Host " - $_" }
    Write-Host ""
    Write-Host ("共 {0} 个文件需要替换。" -f $preview.Count)
}

if (-not $Apply) {
    Write-Host ""
    Write-Host "当前是预览模式，未实际修改文件。"
    Write-Host "执行以下命令进行实际替换："
    Write-Host "  pwsh .\Rename.ps1 -Apply"
    Write-Host ""
    Write-Host "如果还要重命名目录结构，再执行："
    Write-Host "  pwsh .\Rename.ps1 -Apply -RenameDirectories"
    exit 0
}

$changed = @(Replace-InFiles -BaseDir $Root -DoApply:$true)

Write-Host ""
Write-Host ("已完成文本替换，共修改 {0} 个文件。" -f $changed.Count)

if ($RenameDirectories) {
    Rename-PackageDirectories -BaseDir $Root
    Write-Host "已尝试重命名目录结构 com\\arrocean\\dev -> com\\xxx\\dev"
}