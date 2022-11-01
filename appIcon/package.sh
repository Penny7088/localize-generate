#!/bin/bash
export LANG=en_us.UTF-8
export LC_ALL=zh_CN.UTF-8

javapackager -deploy -native dmg -name PLocalize-1.7.0 -BappVersion=1.7.0 -Bicon="./AppIcon.icns" -srcdir . -srcfiles localize-1.7.0-jar-with-dependencies.jar -appclass org.py.localize.main.EasyLocalize -outdir . -outfile localize
