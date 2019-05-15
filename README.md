# BitTorrent Simulator
BitTorrent is a popular P2P protocol for file distribution. This application can be simulated in a single machine or multiple nodes. Following messages are supported in this implementation:
* Handshake
* Choke
* Unchoke
* Interested
* Not Interested
* Have
* Bitfield
* Request
* Piece

## Class Design
![Screenshot](https://github.com/kunwardeeps/bittorrent-simulator/blob/master/flowchart.png)

## Execution
Working Environment:<br>
Unix <br>
To compile:<br>
make<br>
Start Peers remotely:<br>
bash start_remote.sh or java StartRemotePeers<br>
Start Peers locally:<br>
bash start.sh<br>
Stop Peers remotely:<br>
bash stop_remote.sh<br>
