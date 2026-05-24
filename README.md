# Custom Enchantment Upgrader

![CEU](./docs/assets/CEU.png)

[![GitHub](https://img.shields.io/badge/GitHub-CEU-blue?logo=github)](https://github.com/PGS-hwan/CEU)
![Java](https://img.shields.io/badge/Java-21-red?logo=java)
![Gradle](https://img.shields.io/badge/Build-Gradle-green?logo=gradle)
![License](https://img.shields.io/badge/License-GPL3.0-orange?logo=gpl-3.0)

一款强大的 Spigot/Bukkit 服务器插件，提供附魔升级和物品修复功能，支持多种经济插件。

## 项目概述

本插件允许玩家使用服务器经济系统修复手中物品耐久，并对附魔物品执行升级操作。插件支持 XConomy 和 PlayerPoints 两种经济接口。

## 核心功能

- `fix` / `repair`：修复玩家手持物品的耐久
- `upgrade`：提升手持物品附魔等级，支持可配置成功率
- `reload`：重载配置文件
- `admin`：管理员命令，用于修复或升级指定玩家的物品
- 语言文件支持：所有提示消息可通过 `lang.yml` 自定义

## 系统要求

- Java 8 或更高版本
- 1.8.8-1.12.2 Spigot / Paper 服务器
- 软依赖插件：XConomy、PlayerPoints

## 安装指南

1. 使用 Gradle 构建插件 JAR：
   ```bash
   ./gradlew clean build
   ```
2. 将生成的 JAR 文件放入服务器 `plugins` 文件夹
3. 启动或重启服务器
4. 进入 `plugins/CustomEnchantmentUpgrader` 目录，检查并修改 `config.yml` 与 `lang.yml`
5. 执行 `/ceu reload` 以重新加载配置

## 命令说明

### 普通玩家命令

- `/ceu fix` 或 `/ceu repair`
  - 作用：修复当前手持物品的耐久
- `/ceu upgrade`
  - 作用：尝试升级当前手持物品的附魔等级
- `/ceu help`
  - 作用：显示帮助信息

### 管理员命令

- `/ceu admin fix <玩家名>`
  - 作用：为指定玩家修复手持物品
- `/ceu admin repair <玩家名>`
  - 作用：为指定玩家修复手持物品
- `/ceu admin upgrade <玩家名>`
  - 作用：为指定玩家升级手持物品的附魔等级
- `/ceu reload`
  - 作用：重新加载配置文件

### 权限节点

- `ceu.user`：基础用户命令权限
- `ceu.admin`：管理员命令权限

## 配置说明

### `config.yml`

该文件包含插件功能和经济设置。主要配置项包括：

- `economy.type`：选择经济系统，支持 `xconomy` 或 `playerpoints`
- `economy.xconomy.upgrade-cost`：XConomy 升级花费
- `economy.xconomy.fix-cost`：XConomy 修复花费
- `economy.playerpoints.upgrade-cost`：PlayerPoints 升级花费
- `economy.playerpoints.fix-cost`：PlayerPoints 修复花费
- `upgrade.success-chance`：升级成功概率，取值范围 `0` 至 `100`
- `upgrade.max-level`：附魔最大等级
- `upgrade.single-max-level`：单独指定某个附魔的最大等级，例如 `DURABILITY: 5`
- `upgrade.blocked-enchantments`：禁止升级的附魔名称列表
- `debug`：是否启用调试模式，启用后将在控制台输出调试信息

### `lang.yml`

该文件用于自定义插件提示消息及前缀。支持以下键值：

- `prefix`
- `help.title`
- `help.fix`
- `help.repair`
- `help.upgrade`
- `help.admin_fix`
- `help.admin_repair`
- `help.admin_upgrade`
- `help.reload`
- `help.footer`
- `fix.success`
- `fix.no_durability`
- `fix.no_item`
- `fix.insufficient_balance`
- `upgrade.success`
- `upgrade.failed`
- `upgrade.no_enchantment`
- `upgrade.insufficient_balance`
- `upgrade.no_upgradeable`
- `economy.no_system`
- `economy.system_error`
- `economy.xconomy`
- `economy.playerpoints`
- `permission.denied`
- `admin.fix.*`
- `admin.upgrade.*`
- `admin.player_not_found`
- `admin.usage`
- `admin.no_player_name`
- `admin.no_item`
- `admin.unknown_subcommand`
- `reload.success`
- `reload.failed`
- `error.console_only_error`
- `error.unknown_command`
- `error.execute_failed`
- `error.invalid_syntax`

## 构建说明

建议使用项目内置 Gradle Wrapper：

```bash
./gradlew clean build
```

构建生成的插件 JAR 位于 `build/libs/` 目录。

## 项目结构

```
.
├── build.gradle
├── settings.gradle
├── gradle/
├── src/main/java
│   └── com/github/hwan/ceu
├── src/main/resources
│   ├── config.yml
│   ├── lang.yml
│   └── plugin.yml
└── docs/
```

## 依赖项

- Spigot API 1.12.2
- XConomyAPI
- PlayerPoints

## 许可证

本项目使用开源许可证，详情请参见仓库中的许可证信息。
