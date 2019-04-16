#!/bin/bash

while read peerId hostName port hasFile; do
    echo "stopping $peerId"
    ssh -i ~/.ssh/id_rsa ksingh@$hostName "pkill -u ksingh" &
done < PeerInfo.cfg