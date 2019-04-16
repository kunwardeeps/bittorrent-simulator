#!/bin/bash
for peerId in {1001..1005};
do
   sleep 1
   echo "starting $peerId"
   java peerProcess $peerId > $peerId.console.log &
done