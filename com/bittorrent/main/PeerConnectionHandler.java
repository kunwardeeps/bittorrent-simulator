package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.*;
import com.bittorrent.utils.Logger;

import java.io.*;
import java.net.Socket;
import java.util.BitSet;
import java.util.Map;

public class PeerConnectionHandler implements Runnable{

    private Socket peerSocket = null;
    private PeerState peerState = null;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private Logger logger;
    private String remotePeerId;
    private long startTime;
    private long stopTime;

    public PeerConnectionHandler(Socket peerSocket, PeerState peerState) {
        this.peerSocket = peerSocket;
        this.peerState = peerState;
        this.logger = Logger.getLogger(peerState.getPeerId());
    }

    public void setRemotePeerId(String remotePeerId) {
        this.remotePeerId = remotePeerId;
    }

    @Override
    public void run() {
        try
        {
            os = new ObjectOutputStream(peerSocket.getOutputStream());

            Thread asyncMessageSender = new Thread(new AsyncMessageSender(this.peerState.getPeerId(), this));
            asyncMessageSender.start();

            sendMessage(new HandshakeMessage(this.peerState.getPeerId()));

            Message receivedMsg = null;
            while ((receivedMsg = receiveMessage()) != null) {
                System.out.println(this.peerState.getPeerId() + ": Received message type: " +
                        receivedMsg.getMessageType().name() + " from " + this.remotePeerId + ", message: " +
                        receivedMsg.toString());

                switch (receivedMsg.getMessageType()) {
                    case HANDSHAKE: {
                        processHandshake(receivedMsg);
                        break;
                    }
                    case BITFIELD: {
                        processBitField(receivedMsg);
                        break;
                    }
                    case INTERESTED: {
                        processInterested();
                        break;
                    }
                    case NOT_INTERESTED: {
                        processNotInterested();
                        break;
                    }
                    case REQUEST: {
                        processRequest(receivedMsg);
                        break;
                    }
                    case PIECE: {
                        processPiece(receivedMsg);
                        break;
                    }
                    case HAVE: {
                        processHave(receivedMsg);
                        break;
                    }
                    case CHOKE: {
                        break;
                    }
                    case UNCHOKE: {
                        processUnchoke();
                        break;
                    }
                    default:
                        System.out.println("Not implemented!");
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void processUnchoke() {

        int interestingPieceIndex = getNextInterestingPieceIndex(BitTorrentState.getPeers().get(remotePeerId).getBitField(), this.peerState.getBitField());

        if (interestingPieceIndex != -1) {
            RequestMessage requestMessage = new RequestMessage(interestingPieceIndex);
            sendMessage(requestMessage);
        }
    }

    private void processHave(Message receivedMsg) {
        HaveMessage haveMessage = (HaveMessage) receivedMsg;
        int index = (int) haveMessage.getPayload();

        // set peer bitset info
        BitTorrentState.getPeers().get(remotePeerId).getBitField().set(index);

        if (!this.peerState.getBitField().get(index)) {
            System.out.println(this.peerState.getPeerId() + "Sending interested message for piece" + index);
            sendMessage(new InterestedMessage());
        }
    }

    private void processPiece(Message receivedMsg) {
        stopTime = System.currentTimeMillis();
        PieceMessage pieceMessage = (PieceMessage) receivedMsg;
        System.out.println("Received piece index: " + pieceMessage.getIndex());
        byte[] piece = (byte[]) pieceMessage.getPayload();
        setDataRate(piece.length);

        if (piece.length != 0) {
            this.peerState.putFileSplitMap(pieceMessage.getIndex(), piece);
            broadcastMessage(new HaveMessage(pieceMessage.getIndex()));
        }
        else {
            System.out.println("Error: piece length is 0!");
        }
        int index = getNextInterestingPieceIndex(BitTorrentState.getPeers().get(remotePeerId).getBitField(),
                this.peerState.getBitField());
        if (index == -1) {
            NotInterestedMessage notInterestedMessage = new NotInterestedMessage();
            sendMessage(notInterestedMessage);
            FileHandler.writeToFile(this.peerState);
        }
        else {
            System.out.println("Requesting piece Index " + index);
            startTime = System.currentTimeMillis();
            RequestMessage requestMessage = new RequestMessage(index);
            sendMessage(requestMessage);
        }
    }

    private void setDataRate(int size) {
        double dataRate = size / stopTime - startTime;
        BitTorrentState.getPeers().get(remotePeerId).setDataRate(dataRate);
    }

    private void processRequest(Message receivedMsg) {
        RequestMessage requestMessage = (RequestMessage) receivedMsg;
        Integer index = (Integer) requestMessage.getPayload();
        if (this.peerState.getPreferredNeighbours().contains(remotePeerId)) {
            if (this.peerState.getBitField().get(index)) {
                PieceMessage pieceMessage = new PieceMessage(this.peerState.getFileSplitMap().get(index), index);
                sendMessage(pieceMessage);
            }
            else {
                System.out.println("Error: Discarding request message as piece does not exist!");
            }
        }
        else {
            System.out.println("Discarding request message as peer not in preferred neighbour list!");
        }
    }

    private void processInterested() {
        this.peerState.putInterestedNeighbours(remotePeerId);

        if (this.peerState.preferredNeighboursCount() < BitTorrentState.getNumberOfPreferredNeighbors()){
            this.peerState.putPreferredNeighbours(remotePeerId);
        }
    }

    private void processNotInterested() {
        this.peerState.removeInterestedNeighbours(remotePeerId);
    }

    private void processBitField(Message message){
        BitFieldMessage bitFieldMessage = (BitFieldMessage) message;

        BitTorrentState.getPeers().get(remotePeerId).setBitField(bitFieldMessage.getPayload());

        int interestingPieceIndex = getNextInterestingPieceIndex(bitFieldMessage.getPayload(), this.peerState.getBitField());

        if (interestingPieceIndex == -1) {
            NotInterestedMessage notInterestedMessage = new NotInterestedMessage();
            sendMessage(notInterestedMessage);
        }
        else {
            InterestedMessage interestedMessage = new InterestedMessage();
            sendMessage(interestedMessage);
            RequestMessage requestMessage = new RequestMessage(interestingPieceIndex);
            startTime = System.currentTimeMillis();
            sendMessage(requestMessage);
        }

    }

    private int getNextInterestingPieceIndex(BitSet remote, BitSet current) {
        BitSet interestingPieces = new BitSet();
        interestingPieces.or(remote);
        interestingPieces.andNot(current);
        return interestingPieces.nextSetBit(0);
    }

    private void processHandshake(Message response) {
        HandshakeMessage handshakeMessage = (HandshakeMessage) response;
        this.remotePeerId = handshakeMessage.getPeerId();
        if (Integer.parseInt(this.peerState.getPeerId()) < Integer.parseInt(handshakeMessage.getPeerId())) {
            logger.logTcpConnectionFrom(handshakeMessage.getPeerId(), this.peerState.getPeerId());
        }
        if (this.peerState.isHasSharedFile()){
            BitFieldMessage bitfieldMessage = new BitFieldMessage(this.peerState.getBitField());
            sendMessage(bitfieldMessage);
            System.out.println(this.peerState.getPeerId() + " does not have any pieces, so skip sending BITFIELD");
        }
    }

    public Message receiveMessage() {
        try {
            if (is == null) {
                is = new ObjectInputStream(peerSocket.getInputStream());
            }
            return (Message) is.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public synchronized void sendMessage(Message message) {
        System.out.println("Sending " + message.getMessageType().name() + " message: " + message.toString());
        try {
            os.writeObject(message);
            os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(Message message) {
        Map<String, PeerState> peers = BitTorrentState.getPeers();

        for (PeerState peerState : peers.values()){
            if (!peerState.getPeerId().equals(this.peerState.getPeerId())){
                System.out.println("Adding to queue " + peerState.getQueue().add(message));
            }
        }
    }

    public void stop() {
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
