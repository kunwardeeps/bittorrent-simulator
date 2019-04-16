#!/bin/bash

while read peerId hostName port hasFile; do
    echo "starting $peerId"
    ssh -i ~/.ssh/id_rsa -n ksingh@$hostName "cd bittorrent-simulator/; java peerProcess $peerId" > $peerId.console.log &
    sleep 1
done < PeerInfo.cfg