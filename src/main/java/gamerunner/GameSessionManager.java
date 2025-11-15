package gamerunner;

import database.model.GameSession;

public class GameSessionManager {
    private GameSession session;

    public GameSessionManager(GameSession session) {
        this.session = session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public GameSession getSession() { return session; }
}
