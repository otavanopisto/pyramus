#!/usr/bin/env python
import sys
import os
import os.path
import xml.etree.ElementTree as ET

ns = 'http://maven.apache.org/SETTINGS/1.0.0'

if os.environ["TRAVIS_SECURE_ENV_VARS"] == "false":
  print("no secure env vars available, skipping deployment")
  sys.exit()

homedir = os.path.expanduser("~")

ET.register_namespace('', ns)
m2 = ET.parse(homedir + '/.m2/settings.xml')
settings = m2.getroot()
servers = settings.find('s:servers', {'s': ns})
if servers is None:
    servers = ET.SubElement(settings, 'servers')

oo_snapshots = ET.SubElement(servers, 'server')
ET.SubElement(oo_snapshots, 'id').text = 'otavanopisto-snapshots'
ET.SubElement(oo_snapshots, 'username').text = os.environ['OOM2_USERNAME']
ET.SubElement(oo_snapshots, 'password').text = os.environ['OOM2_PASSWORD']

oo_releases = ET.SubElement(servers, 'server')
ET.SubElement(oo_releases, 'id').text = 'otavanopisto-releases'
ET.SubElement(oo_releases, 'username').text = os.environ['OOM2_USERNAME']
ET.SubElement(oo_releases, 'password').text = os.environ['OOM2_PASSWORD']

# mirrors = settings.find('s:mirrors', {'s': ns})
# if mirrors is None:
#     mirrors = ET.SubElement(settings, 'mirrors')
# 
# codehaus_snapshots = ET.SubElement(mirrors, 'mirror')
# ET.SubElement(codehaus_snapshots, 'id').text = 'codehaus-snapshots-mirror'
# ET.SubElement(codehaus_snapshots, 'name').text = 'Codehaus snapshots mirror'
# ET.SubElement(codehaus_snapshots, 'url').text = 'https://oss.sonatype.org/content/repositories/codehaus-snapshots/'
# ET.SubElement(codehaus_snapshots, 'mirrorOf').text = 'codehaus-snapshots'

m2.write(homedir + '/.m2/mySettings.xml',
         xml_declaration = True,
         encoding = 'utf-8',
         method = 'xml')
