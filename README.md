# Toothpick
A light Kotlin library that replicates ButterKnife's function

## Usage
```kotlin
val mButton: Button by bind(R.id.button_press_me) //simple lazy delegation

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Toothpick.bind(this) // use this when you bind @OnClick
}

@OnClick(R.id.button_press_me) fun showToast() {
    // ...
}
```

