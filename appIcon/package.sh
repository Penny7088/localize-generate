#!/bin/bash
export LANG=en_us.UTF-8
export LC_ALL=zh_CN.UTF-8

javapackager -deploy -native dmg -name Localize -BappVersion=1.2.1 -Bicon="./AppIcon.icns" -srcdir . -srcfiles localize-1.2.1-jar-with-dependencies.jar -appclass org.py.localize.main.EasyLocalize -outdir . -outfile localize
