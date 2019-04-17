#!/bin/bash

while read peerId hostName port hasFile; do
    echo "starting $peerId"
    java peerProcess $peerId > $peerId.console.log &
    sleep 1
done < PeerInfo.cfg
