/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorlogicalclock;

/**
 *
 * @author Ian Gortan
 */
public class Message {
    private int pid;
    private int[] clocks = new int[3];
    private String messageID;
    
    /**
     * Constructor
     * @param pid
     * @param clocks
     * @param message 
     */
    public Message (int pid, int[] clocks, String message) {
        this.pid = pid;
        this.setClocks(clocks);
        this.messageID = message;
    }
    /**
     *  Copy constructor
     */
    public Message (Message aMessage) {
        this.pid = aMessage.getPid();
        this.setClocks(aMessage.getClocks());
        this.messageID = aMessage.getMessageID();
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * @return the clock
     */
    public int[] getClocks() {
        return clocks;
    }

    /**
     * @param clocks the clock to set
     */
    public void setClocks(int[] clocks) {
        for (int i = 0; i < 3; i++) {
            this.clocks[i] = clocks[i];
        }
    }

    /**
     * @return the messageID
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
    
}
