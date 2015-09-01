AndroidCountryPicker
====================

Updated to use the Gradle build system and hosted on JitPack for ease of inclusion in Gradle enabled projects:

https://jitpack.io/#deano2390/AndroidCountryPicker

# Gradle
--------
/build.gradle
```groovy
allprojects {
	repositories {
		jcenter()
		maven { url "https://jitpack.io" }
	}
}
```

Then add the dependency to your module's build.gradle:

/app/build.gradle
```groovy
compile 'com.github.deano2390:AndroidCountryPicker:1.0.1'
```

NOTE: Some people have mentioned that they needed to add the @aar suffix to get it to resolve from JitPack:
```groovy
compile 'com.github.deano2390:AndroidCountryPicker:1.0.1@aar'
```



Original ReadMe
====================

## Features
CountryPicker is a simple fragment that can be embedded or shown as dialog. See the example to see more detail.


<img src="https://raw.github.com/roomorama/AndroidCountryPicker/master/screenshot/1.png" width="250">
<img src="https://raw.github.com/roomorama/AndroidCountryPicker/master/screenshot/2.png" width="250">


The functions are simple:
 
1) Allow user to search the country

2) Inform client which country user has selected

3) Convenient function to get currency code of the selected country

## How to use

To embed CountryPicker in your own view:

```java
FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
CountryPicker picker = new CountryPicker();
transaction.replace(R.id.home, picker);
transaction.commit();
```

To show CountryPicker as a dialog:

```java
CountryPicker picker = CountryPicker.newInstance("Select Country");
picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
```

When user selects a country, client can listen to that event:

```java
picker.setListener(new CountryPickerListener() {

	@Override
	public void onSelectCountry(String name, String code) {
		// Invoke your function here
	}
});
				
```

## About
The data is from CountryPicker by nicklockwood (https://github.com/nicklockwood/CountryPicker)

I converted his data in "Countries.plist" to json format to avoid extra dependency.

Thanks Nick for his awesome library!

## License
See LICENSE.md
