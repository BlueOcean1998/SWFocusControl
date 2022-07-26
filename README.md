# FocusControl
焦点控制框架，用于Android TV应用的焦点控制。  
FocusControl集成了以下仓库：
```groovy
    api 'androidx.constraintlayout:constraintlayout:2.1.2'
    api 'androidx.recyclerview:recyclerview:1.2.0'
```
FocusControl主要由FocusControlLinearLayout & FocusControlRelativeLayout  
& FocusControlConstraintLayout & FocusControlRecyclerView组成。  
它们都继承了FocusControlViewParent接口。  
FocusControl提供了一系列焦点控制的方法，极大地方便了Android TV应用的焦点控制。
## 1.FocusControlLinearLayout；
### (1).recordFocusEnabled & defFocusId & lastFocusView
在实际场景中，LinearLayout和RecyclerView经常需要记录焦点。  
如：在一个页面中有2列子页面。左边一列是导航栏，有许多item，如果item很多还要求可以滚动，  
右边一列根据左边选择的item切换对应页面，也有可能有需要item并需要滚动。  
这样的页面通常需要使用LinearLayout+ScrollView & RecyclerView来写。  
如果使用系统默认寻焦，从切到右时，右边的item就会因左边的item的位置不同而聚焦到不同的位置，  
切回左边的item时也会因为右边的item的位置不同而聚焦到不同的位置，  
而且会因切到左边的另一个item导致右边的页面发送变化。这是非常影响用户体验的。  
FocusControlLinearLayout提供3个属性来解决上述场景中的问题  
recordFocusEnabled：是否记录最后聚焦控件  
若该属性为true，当焦点回到布局中时，会自动聚焦回布局之前最后聚焦的控件上。  
defFocusId：默认聚焦控件id  
其设置为布局的子控件id时，首次聚焦到布局中会自动聚焦到id指示的控件上。  
lastFocusView：最后聚焦控件  
当焦点在布局内发生变化时，lastFocusView会被设置为上一个聚焦的控件。  
你可以在xml或代码中设置\获取它们。  
示例代码：
xml:
```xml
    <net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout
        android:id="@+id/fcl_root"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:defFocusId="@id/btn_1">
        ...
    </net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout>
```
kotlin:
```kotlin
    viewBinding.run {
        llRoot.recordFocusEnabled = false
        llRoot.defFocusId = R.id.btn_1
        llRoot.lastFocusView = btn1
        val recordFocusEnabled = llRoot.recordFocusEnabled
        val defFocusId = llRoot.defFocusId
        val lastFocusView = llRoot.lastFocusView
    }
```
java:
```java
    viewBinding.llRoot.setRecordFocusEnabled(false)
    viewBinding.llRoot.setDefFocusId(R.id.btn_1)
    viewBinding.llRoot.setLastFocusView(viewBinding.btn1)
    boolean recordFocusEnabled = viewBinding.llRoot.getRecordFocusEnabled()
    int defFocusId = viewBinding.llRoot.getDefFocusId()
    View lastFocusView = viewBinding.llRoot.getLastFocusView()
```
注意：  
1.在FocusControlLinearLayout中，recordFocusEnabled默认为true；  
2.若recordFocusEnabled设置为false，当焦点在布局内发生变化时，lastFocusView不会记录最后聚焦控件；  
3.若recordFocusEnabled设置为false
或lastFocusView为空或无法正常聚焦（如首次聚焦到布局中或lastFocusView被隐藏或移除），  
则聚焦到defFocusId对应的控件上；  
若没有设置defFocusId或无法找到defFocusId对应的控件（如错误设置为其它布局的控件id），  
则聚焦到首个可聚焦控件上。
### (2).searchFocusLeft & searchFocusRight & searchFocusUp & searchFocusDown
在实际场景中，焦点经常会出现不应跳转到某个位置或无法跳转到预定位置的情况。  
如：有2排布局，每一排都有1个文本框和1个输入框，上面一排的输入框要比下面一排的长一些。  
当焦点在下面一排的输入框上时，按右键，焦点会跳转到上面一排的输入框中。这仅仅是因为上面一排的输入框长那么一些。  
又如：参照1.(1)中的场景。现在右边一列不再是只有一列item，而是由一列item和若干控制按钮组成的复杂页面。  
我想让焦点从左边跳转到右边时，每次都要在第一个控制按钮上。可是因为item列表占的位置大，焦点总是被它捕获。  
如果用nextFocusRight来控制，那么左边一列的每个item都要设置nextFocusRight，非常麻烦。  
FocusControlLinearLayout提供4个属性来解决上述场景中的问题  
在上述场景1中，你可以通过将searchFocusRight设置为nullSearchFocus阻止焦点跳转到上面一排的输入框。  
示例代码：
```xml
    <net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:searchFocusRight="nullSearchFocus">
        ...
    </net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout>
```
在上述场景2中，你可以通过将searchFocusRight设置为你想要聚焦的控制按钮id解决问题。  
示例代码：
```xml
    <net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:orientation="vertical"
        app:searchFocusRight="@id/btn_1">
        ...
    </net.sunniwell.aar.focuscontrol.layout.FocusControlLinearLayout>
```
注意：  
1.recordFocusEnabled设置设置为false时，searchFocus也生效；  
2.searchFocus的默认值为defSearchFocus，设置为该值则使用(1)中的聚焦方式；  
3.你可以将searchFocus设置为systemSearchFocus以使用系统自动寻焦；  
4.searchFocus也可以通过代码来设置\获取。
## 2.FocusControlRelativeLayout & FocusControlFrameLayout & FocusControlConstraintLayout；
焦点控制RelativeLayout FrameLayout ConstraintLayout。  
它们的使用方式和FocusControlLinearLayout是一致的，  
但因为它们需要记录焦点的场景不多，因此它们的recordFocusEnabled默认为false。
## 3.FocusControlScrollView
焦点控制ScrollView。  
它的使用方式和FocusControlLinearLayout是一致的，recordFocusEnabled默认为true。  
它会使你的ScrollView滚动时保持聚焦的item在中间，将isHoldFocusInCenter设置为false可关闭此功能。
## 4.FocusControlRecyclerView；
### (1).FocusControlRecyclerView
焦点控制RecyclerView。  
它的使用方式和上述4个焦点控制布局基本上是一致的，其recordFocusEnabled默认为true。  
在实际场景中，RecyclerView只有在焦点到达边缘时才会滚动。这并不符合部分场景的UI需求。  
很多时候需要预留一些item给用户查看下一个item是什么才能带给用户更好的使用体验。  
又如在一些场景中需要将聚焦的item固定在中间，原生的RecyclerView是无法实现的。  
FocusControlRecyclerView提供了boundaryItemCount属性来控制焦点到达边界时自动滚动。  
如，将boundaryItemCount设置为3，则滚动方向上还剩余3个可见的item时，自动向方向上滚动，  
保持剩余可见的item数量始终为3，直到到达FocusControlRecyclerView的边缘。  
你可以在xml或代码中设置\获取它。  
示例代码：
xml:
```xml
    <net.sunniwell.aar.focuscontrol.layout.FocusControlRecyclerView
        android:id="@+id/focus_control_rv_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:boundaryItemCount="3"
        app:searchFocusDown="nullSearchFocus"
        app:searchFocusUp="nullSearchFocus" />
```
kotlin:
```kotlin
    viewBinding.focusControlRv1.boundaryItemCount = 3
    val boundaryItemCount = viewBinding.focusControlRv1.boundaryItemCount
```
java:
```java
    viewBinding.focusControlRv1.setBoundaryItemCount(3)
    int boundaryItemCount = viewBinding.focusControlRv1.getBoundaryItemCount()
```
boundaryItemCount默认为3。  
不建议将boundaryItemCount设置的更低，因为这可能会引发adapter在notify时移动焦点导致丢失焦点的问题。  
此外，FocusControlRecyclerView还提供了许多属性直接获取某些值，  
而不用通过layoutManager或adapter间接获取它们了。  
如：  
orientation：方向  
itemCount：item总数  
spanCount：跨度（行\列）数  
注意，目前FocusControlRecyclerView只适配了  
LinearLayoutManager & GridLayoutManager & StaggeredGridLayoutManager 3种layoutManager。  
你只能设置layoutManager为它们或它们的子类。但这已经足够覆盖所有使用场景了。
### (2).FocusControlAdapter
焦点控制Adapter。  
该类会自动重写getItemId(position: Int)，并自动调用setHasStableIds(true)。  
这样adapter在调用notify方法时，recyclerView就不会丢失焦点了。
### (3).FocusControlLayoutManager
焦点控制LayoutManager。  
在设置focusControlRecyclerView的layoutManager时，建议使用  
FocusControlGridLayoutManager & FocusControlLinearLayoutManager & StaggeredGridLayoutManager。  
它们会使你的recyclerView滚动时保持聚焦的item在中间，将isHoldFocusInCenter设置为false可关闭此功能。  
它们会使你的recyclerView在item数量、位置、内容发生改变时保持原本的焦点位置。  
此外，FocusControlLayoutManager会略微降低smoothScrollToPosition方法的滚动速度。  
你可以通过设置scrollSpeed属性来调整smoothScrollToPosition的滚动速度。  
示例代码：
kotlin:
```kotlin
    focusControlLinearLayoutManager.scrollSpeed = 
        FocusControlLinearLayoutManager.SCROLL_SPEED_FAST
```
java:
```java
    focusControlLinearLayoutManager.setScrollSpeed(
            FocusControlLinearLayoutManager.SCROLL_SPEED_FAST)
```
有4个速度等级可供选择。  
其中normal为默认速度，fast为原本的速度，immediately为立刻滚动到指定位置。  
你也可以自定义其它速度。
## 5.FocusControlViewParent；
焦点控制父控件接口。  
上述布局都继承了FocusControlViewParent。  
FocusControlViewParent提供了recordFocusEnabled & defFocusId & lastFocusView &  
searchFocusLeft & searchFocusRight & searchFocusUp & searchFocusDown  
这些“抽象属性”。  
kotlin语言会自动为它们在接口中生成get set抽象方法，  
并在重写它们的类中自动重写get set方法并生成对应属性对象。  
上述布局在继承这些抽象属性时使用了final关键字，即若你再继承这些布局时不必、也不能再重写它们了。  
FocusControlViewParent默认实现了2个方法：  
kotlin:
```kotlin
    findNextFocus(
        systemNextFocus: View?, focused: View?, direction: Int
    ): View? {
        ...
    }
    findNextFocus(
        systemNextFocus: View?, focused: View?,
        nextFocusParents: ArrayList<FocusControlViewParent>
    ): View? {
        ...
    }
```
它们是实现焦点控制的关键。  
第1个方法用于控制4个searchFocus。  
第2个方法用于控制recordFocusEnabled & defFocusId & lastFocusView。  
第1个方法调用了第2个方法
（即在继承FocusControlViewParent时，只需要调用第1个方法就能实现所有焦点功能了）。  
如果你有更多ViewGroup需要控制焦点，在继承FocusControlViewParent时实现焦点控制的关键如下：
kotlin:
```kotlin
    override fun focusSearch(focused: View?, direction: Int): View? {
        if (recordFocusEnabled) lastFocusView = focused
        return findNextFocus(super.focusSearch(focused, direction), focused, direction)
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        super.requestChildFocus(child, focused)
        if (recordFocusEnabled) lastFocusView = child
    }
```
重写ViewGroup的focusSearch方法和requestChildFocus方法，然后：  
1.在focusSearch方法中记录焦点并返回FocusControlViewParent提供的第1个findNextFocus方法获取的控件；  
2.在requestChildFocus方法中记录焦点。  
注意：  
在应用场景中，请全部使用焦点控制框架提供的ViewGroup。  
因为焦点控制是在寻焦时进行的，而不是获取焦点时。  
即使是单个View，也要为其套上FocusControlFrameLayout。
## 6.FocusControlUtil；
焦点控制工具类。提供一些焦点控制方法。  
FocusControlViewParent和FocusControlRecyclerView引用了部分该类的方法。  
如，你可以通过View.focusControlViewParent获取控件的FocusControlViewParent。
## 7.FocusControlLogUtil。
FocusControl使用FocusControlLogUtil来控制日志打印。  
如果需要进行调试，可以将打印等级level设置为ALL。  
调试完成发布版本前，别忘记将level设置回NONE。  
最后，焦点控制问题是非常复杂的，不同场景下使用FocusControl的方式也需要具体分析，  
FocusControl也并不一定能解决所有问题。

Finish on 2021-07-21
