#!/usr/bin/env python
import sys
import os
import os.path
import xml.dom.minidom

if os.environ["TRAVIS_SECURE_ENV_VARS"] == "false":
  print "no secure env vars available, skipping deployment"
  sys.exit()

homedir = os.path.expanduser("~")

m2 = xml.dom.minidom.parse(homedir + '/.m2/settings.xml')
settings = m2.getElementsByTagName("settings")[0]

serversNodes = settings.getElementsByTagName("servers")
if not serversNodes:
  serversNode = m2.createElement("servers")
  settings.appendChild(serversNode)
else:
  serversNode = serversNodes[0]

ooServerNode = m2.createElement("server")
ooServerId = m2.createElement("id")
ooServerUser = m2.createElement("username")
ooServerPass = m2.createElement("password")

idNode = m2.createTextNode("otavanopisto-snapshots")
userNode = m2.createTextNode(os.environ["OOM2_USERNAME"])
passNode = m2.createTextNode(os.environ["OOM2_PASSWORD"])

ooServerId.appendChild(idNode)
ooServerUser.appendChild(userNode)
ooServerPass.appendChild(passNode)

ooServerNode.appendChild(ooServerId)
ooServerNode.appendChild(ooServerUser)
ooServerNode.appendChild(ooServerPass)

serversNode.appendChild(ooServerNode)

m2Str = m2.toxml()
f = open(homedir + '/.m2/trasettings.xml', 'w')
f.write(m2Str)
f.close()