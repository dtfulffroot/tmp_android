# 参考文章


[安卓11中的存储机制更新](https://developer.android.com/about/versions/11/privacy/storage?hl=zh-cn#media-direct-file-native)

下面这个链接是直接获取比较高的权限, 就是授予安卓应用能够访问外部路径的能力.

[管理存储设备上的所有文件](https://developer.android.com/training/data-storage/manage-all-files?hl=zh-cn#directory-access)

里面说的几步也比较简单, 下面是一个申请的例子, (没有回调函数, 仅仅指出需要使用的方法):
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    if (!Environment.isExternalStorageManager()) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivity(intent);
    }
}
```

[使用的github截取图片的轮子](https://github.com/Yalantis/uCrop)

[android studio官方API查询网站](https://developer.android.com/reference/)


