@echo off
cd bin

echo Generating Jar File...
jar cmf ../META-INF/MANIFEST.MF ../build/%1.jar *
echo Jar File Created.

cd ../build/

echo Generating Classes.dex...
call dx --dex --output=classes.dex %1.jar %2
echo Classes.dex Created.

echo Putting Classes.dex Into the Jar...
call aapt add %1.jar classes.dex
echo CAC Finished.

echo Deleting Classes.dex from the local files...
call del classes.dex
echo Classes.dex Deleted

echo Pushing files to device...
call adb push %1.jar /sdcard/Android/data/br.ufc.great.loccam/files/components/
echo CAC Pushed

echo ----------
echo DONE.

cd ../
