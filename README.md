# Instagram-Gallery-Library
I have created a simple library which will show gallery view like instagram
Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency

```
dependencies {
        implementation 'com.github.durgeshrjkumar:Instagram-Gallery-Library:1.0'
}
```
To Get Data
```
Intent intent1 = new Intent(context, TGallery.class);
intent1.putExtra("selectMultiple",true);
startActivityForResult(intent1,LAUNCH_GALLERY_IMAGE);
```

onActivityResult

               ```
ArrayList<File> lstData = (ArrayList<File>) data.getSerializableExtra("fileList");
                if (lstData.size() > 0) {
                    Picasso.get().load(lstData.get(0)).into(imgOrder);
               }
```
