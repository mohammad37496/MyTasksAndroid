$ErrorActionPreference = "Stop"

$RepoPath = "D:\AndroidProjects\MyTasksAndroid"
$IntervalSeconds = 30

if (-not (Test-Path (Join-Path $RepoPath ".git"))) {
    Write-Host "Repository not found: $RepoPath"
    exit 1
}

Set-Location $RepoPath

git config pull.ff only

while ($true) {
    try {
        $status = git status --porcelain

        if ([string]::IsNullOrWhiteSpace($status)) {
            git fetch origin main --quiet

            $local = (git rev-parse HEAD).Trim()
            $remote = (git rev-parse origin/main).Trim()

            if ($local -ne $remote) {
                git pull --ff-only origin main

                $now = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
                Write-Host "[$now] Updated from GitHub."
            }
        }
        else {
            $now = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            Write-Host "[$now] Local changes detected; sync skipped to protect your files."
        }
    }
    catch {
        $now = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
        Write-Host "[$now] Sync error: $($_.Exception.Message)"
    }

    Start-Sleep -Seconds $IntervalSeconds
}
