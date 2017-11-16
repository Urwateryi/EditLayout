# EditLayout

## 自定义编辑控件

最近工作不忙，对于之前像那种个人中心的编辑页面，以前的做法要么是使用RecyclerView，要么是一行一行的画，这次我突发奇想，想着整个项目里，这种地方还是挺多的，就干脆自定义一个算了，每次一行就是一个这个控件得了

## 常见需求

类似以下这种界面，太多了，但总归是首先分为

- 有没有二级页面
- 内容靠左还是靠右
- 内容是用EditText还是TextView

![image](https://raw.githubusercontent.com/Urwateryi/MarkDownPic/master/%E9%9C%80%E6%B1%82.jpg)
![image](https://raw.githubusercontent.com/Urwateryi/MarkDownPic/master/%E9%9C%80%E6%B1%822.jpg)

![image](https://raw.githubusercontent.com/Urwateryi/MarkDownPic/master/%E9%9C%80%E6%B1%823.jpg)

另外我在app的build.gradle里引入了

java8

```
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
}
```

和张鸿洋的AutoLayout

https://github.com/hongyangAndroid/AndroidAutoLayout

```
compile 'com.zhy:autolayout:1.4.5'
```