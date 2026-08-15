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
| **GitHub → ローカル**（最新を取る） | `scripts\sync-port.bat` または `scripts\sync-port.bat pull` |
| **ローカル → GitHub**（自分のコミットを送る） | `scripts\sync-port.bat push` |
| 差分の確認だけ | `scripts\sync-port.bat status` |

例:

```bat
cd C:\src\Invasion-mod
scripts\sync-port.bat pull
```

成功すると `port/1.20.1` が `origin/port/1.20.1` に fast-forward されます。

## 推奨の日常フロー

1. 作業前: `scripts\sync-port.bat pull`
2. コード編集・ビルド・VoxPilot
3. （自分でコミットした場合）`scripts\sync-port.bat push`
4. エージェント側が GitHub に push した変更を取り込む: また `pull`

## トラブル

### `Permission denied (publickey)`

- GitHub に登録した SSH 公開鍵と、PC の秘密鍵が一致しているか確認
- `ssh -T git@github.com` が成功するか確認

### Fast-forward できない

ローカルとリモートが分岐しています。

```bat
git log --oneline --left-right origin/port/1.20.1...HEAD
```

- ローカルを捨ててリモートに合わせる:  
  `git reset --hard origin/port/1.20.1`
- ローカルを残して載せ直す:  
  `git pull --rebase origin port/1.20.1` のあと `push`

### 未コミットの変更がある

`pull` 前に `git status` を確認。必要なら:

```bat
git stash -u
scripts\sync-port.bat pull
git stash pop
```
