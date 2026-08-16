# ローカル ↔ GitHub 同期（port/1.20.1）

SSH で GitHub と連携済みなら、HTTPS / PAT は不要です。

## 初回だけ（SSH に切り替える）

リポジトリが `https://...` のままだと、SSH 鍵が使われません。

```bat
cd C:\src\Invasion-mod
scripts\sync-port.bat use-ssh
```

確認:

```bat
git remote -v
```

次のようになっていれば OK です。

```text
origin  git@github.com:Misosiruzuki/Invasion-mod.git (fetch)
origin  git@github.com:Misosiruzuki/Invasion-mod.git (push)
```

## いつも使うコマンド

| 目的 | コマンド |
|------|----------|
| **GitHub → ローカル（上書き同期）** | `scripts\sync-port.bat` または `scripts\sync-port.bat pull` |
| **ローカル → GitHub** | `scripts\sync-port.bat push` |
| 差分の確認だけ | `scripts\sync-port.bat status` |

例:

```bat
cd C:\src\Invasion-mod
scripts\sync-port.bat pull
```

### pull の動作（上書き）

1. `git fetch origin port/1.20.1`
2. `git checkout -f -B port/1.20.1 origin/port/1.20.1`（衝突する untracked も強制）
3. `git reset --hard origin/port/1.20.1`
4. `git clean -fd`（**gitignore されていない** untracked を削除）

ローカルの未コミット変更・独自コミットは破棄されます。  
`run/` や `.gradle/` など **gitignore 済み**のものは残ります（VoxPilot レポートなど）。

成功時の目安:

```text
## port/1.20.1...origin/port/1.20.1
a7466ce ...
```

## 推奨の日常フロー

1. 作業前: `scripts\sync-port.bat pull`
2. コード編集・ビルド・VoxPilot
3. （自分でコミットした場合）`scripts\sync-port.bat push`
4. エージェント側の push を取り込む: また `pull`

## トラブル

### `Permission denied (publickey)`

- `ssh -T git@github.com` が成功するか確認

### どうしても汚いツリーになる

```bat
git fetch origin
git checkout -f -B port/1.20.1 origin/port/1.20.1
git reset --hard origin/port/1.20.1
git clean -fd
```

またはフォルダを作り直す:

```bat
cd C:\src
ren Invasion-mod Invasion-mod-old
git clone -b port/1.20.1 git@github.com:Misosiruzuki/Invasion-mod.git Invasion-mod
```
