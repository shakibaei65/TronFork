package org.tron.core.net.messagehandler;

import org.tron.core.exception.P2pException;
import org.tron.core.net.message.AloneMessage;
import org.tron.core.net.peer.PeerConnection;

public interface AloneMsgHandler {

  void processMessage(PeerConnection peer, AloneMessage msg) throws P2pException;

}
