# LockScreenEdit - 添加新样式指南

### 1. 添加预览图片
在 `app/src/main/res/drawable/` 目录下添加你的样式预览图片。
- 文件名格式：`preview_style_X.png` (X为样式编号)
- 推荐尺寸：符合手机屏幕比例 (9:16)
- 格式：PNG, JPG, WEBP

### 2. 添加样式名称和描述
在 `app/src/main/res/values/strings.xml` 文件中添加样式名称和描述：
```xml
<string name="lockscreen_style_your_name">你的样式名称</string>
<string name="style_your_description">你的样式描述</string>
```

### 3. 更新样式配置
在 `app/src/main/java/org/avium/lockscreenedit/config/StyleConfig.kt` 文件中的 `availableStyles` 列表里添加新配置：

```kotlin
LockscreenStyle(
    id = 3,  
    nameResId = R.string.lockscreen_style_your_name, 
    previewResId = R.drawable.preview_style_3, 
    descriptionResId = R.string.style_your_description 
)
```

## 注意事项

- **ID 必须唯一**：每个样式的 ID 必须是唯一的，从 1 开始递增
- **文件命名规范**：建议使用统一的命名规范以便管理
- **预览图质量**：预览图应该清晰地展示样式特点

## 系统属性说明

当用户选择样式时，系统会设置以下属性：
- `persist.avium.customlockscreen.enable`: true/false 启用状态
- `persist.avium.customlockscreen.type`: 样式类型 ID (1, 2, 3...)
- `persist.avium.customlockscreen.color`: 总体颜色 (FFFFFF 或 "blur")
- `persist.avium.customlockscreen.hour.color`: 小时颜色 (16进制)
- `persist.avium.customlockscreen.minute.color`: 分钟颜色 (16进制)

## 完成！
