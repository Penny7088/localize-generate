#!/bin/bash
export LANG=en_us.UTF-8
export LC_ALL=zh_CN.UTF-8

javapackager -deploy -native dmg -name Localize -BappVersion=1.0 -Bicon=package/macosx/AppIcon.icns -srcdir . -srcfiles localize-1.0.0-jar-with-dependencies.jar -appclass org.yzr.poi.main.EasyLocalize -outdir . -outfile 123
