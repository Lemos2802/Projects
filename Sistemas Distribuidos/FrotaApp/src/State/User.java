package State;

import java.util.concurrent.locks.ReentrantLock;

public class User {
    ReentrantLock l = new ReentrantLock();
    private String username;
    private String password;
    private boolean notificacoes;

    public User (String username, String password) {
        this.username = username;
        this.password = password;
        this.notificacoes = false;
    }
    
    public Boolean getNotificacoes() {
        try{
            l.lock();
            return notificacoes;
        }
        finally {
            l.unlock();
        }
    }

    public void setON_OFF(boolean n) {
        try {
            l.lock();
            this.notificacoes = n;
        }
        finally {
            l.unlock();
        }
    }
    
    
    public String getUsername() {
        try{
            l.lock();
            return username;
        }
        finally {
            l.unlock();
        }
    }

    public void setUsername(String username) {
        try {
            l.lock();
            this.username = username;
        }
        finally {
            l.unlock();
        }
    }

    public String getPassword() {
        try{
            l.lock();
            return password;
        }
        finally {
            l.unlock();
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
