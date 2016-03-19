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

## Download
Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
and add to dependencies
```groovy
    dependencies {
	        compile 'com.github.allan1st.Toothpick:annotation:0.5.0'
    		compile 'com.github.allan1st.Toothpick:toothpick:0.5.0'
    		kapt 'com.github.allan1st.Toothpick:compiler:0.5.0'
	}
```

## License
> The MIT License (MIT)
> 
> Copyright (c) 2016 Yilun Chen
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy
> of this software and associated documentation files (the "Software"), to deal
> in the Software without restriction, including without limitation the rights
> to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the Software is
> furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all
> copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
> FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
> AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
> LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
> OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
> SOFTWARE.
