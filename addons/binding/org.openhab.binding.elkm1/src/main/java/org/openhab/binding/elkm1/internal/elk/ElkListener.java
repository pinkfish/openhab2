package org.openhab.binding.elkm1.internal.elk;

/**
 * Handles the elk messages generate from the elk system.
 *
 * @author David Bennett - Initial Contribution
 */
public interface ElkListener {
    void handleElkMessage(ElkMessage message);
}
