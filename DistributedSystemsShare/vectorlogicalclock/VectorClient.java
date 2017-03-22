/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorlogicalclock;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ian Gortan
 */
public class VectorClient implements Runnable {
    
    private MessageBuffer buffer;
    final private int NUM_EVENTS = 10; 
    private int pid;
    private int[] timestamps = new int[3];
    

    public VectorClient(MessageBuffer buf, int pid) {
        this.buffer = buf;
        this.pid = pid;
    }

    public void run() {
        Random random = new Random();
        //start at -1 just so we can index in to events array easily
        timestamps[pid] = -1;
        ArrayList<String> events  = new ArrayList<String> ();         
        
        // create a message object
        Message m = new Message(pid, timestamps, "init");
//        m.setPid(pid);
//        m.setClock(clock) ;

        // add some 'reandomness' to the order at startup
        if ( (pid%2) == 0 ) {
            timestamps[pid]++;
            m.setClocks(timestamps);
            String event = "[INTERNAL] "  + "PID="  + Integer.toString(pid) + " CLOCK:" + toStringRep(timestamps);
            events.add(event);
        }
        for (int i = 0; i < NUM_EVENTS ; i++ ) {
            // let's generate an event
            timestamps[pid]++;
            m.setClocks(timestamps);
            if ( ((i%2) == 0) ) {
                // internal event
                String event = "[INTERNAL] "  + "PID="  + Integer.toString(pid) + " CLOCK:" + toStringRep(timestamps);
                events.add(event);
            } else {
                // external event
                String event = "[SEND]     " + "PID="  + Integer.toString(pid) + " CLOCK:" +toStringRep(timestamps);
                events.add(event);
                m.setMessageID(event);
                buffer.put(m); // sleep for a random time to induce different orders
                try {
                      Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException e) {}

                // get a message and set clock according to Lamport algorithm
                //System.out.println("Getting a message: " + Integer.toString(pid));
                Message recMsg = buffer.get(pid);
                 // chck it wasn't a message we sent (null returned) as we want to ignore those
                 
                if (recMsg != null) {
                    // add the event ...
                    timestamps[pid]++;
                    for (int j = 0; j < 3; j++) {
                        timestamps[j] = Math.max(timestamps[j], recMsg.getClocks()[j]);
                    }
                    event = "[RECEIVE]  " + "FROM PID="  + Integer.toString(recMsg.getPid()) + " CLOCK IN:" + toStringRep(recMsg.getClocks()) + " LOCAL CLOCK " + toStringRep(timestamps);
                    events.add(event);
                }
                
                
            }
           
                
        }
        
        System.out.println("Process " + pid + " complete and ending");
        // your code
        for (String event: events) {
            System.out.println(event);
        }
            
          
     }

     private String toStringRep(int[] times) {
         StringBuffer res = new StringBuffer();
         res.append('[');
         for (int i = 0; i < 3; i++) {
             res.append(times[i]).append(',');
         }
         res.deleteCharAt(res.length() - 1);
         res.append(']');
         return res.toString();
     }
 
}
    

