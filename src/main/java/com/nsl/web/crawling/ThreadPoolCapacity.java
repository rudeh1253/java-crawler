package com.nsl.web.crawling;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadPoolCapacity {
    private final Set<ThreadTicket> THREAD_TICKETS = ConcurrentHashMap.newKeySet();

    public ThreadTicket getThreadTicket() {
        for (ThreadTicket ticket : THREAD_TICKETS) {
            if (!ticket.isPublished()) {
                return publishTicket(ticket);
            }
        }
        ThreadTicket newTicket = new ThreadTicket();
        THREAD_TICKETS.add(newTicket);
        return publishTicket(newTicket);
    }

    private ThreadTicket publishTicket(ThreadTicket ticket) {
        ticket.publish();
        return ticket;
    }

    public void retrieveTicket(ThreadTicket ticket) {
        ticket.getBack();
    }

    public boolean didAllTicketsRetrieve() {
        for (ThreadTicket ticket : THREAD_TICKETS) {
            if (ticket.isPublished()) {
                return false;
            }
        }
        return true;
    }

    public class ThreadTicket {
        private boolean pub = false;

        private void publish() {
            this.pub = true;
        }

        private void getBack() {
            this.pub = false;
        }

        private boolean isPublished() {
            return this.pub;
        }
    }
}
