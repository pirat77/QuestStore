package service;

import DAO.QuestDAO;
import model.elements.Quest;

import java.util.List;

public class QuestService {
    QuestDAO questDao;
    static QuestService questServiceInstance;

    QuestService(){
        this.questDao = new QuestDAO();
    }

    static public  QuestService getInstance(){
        if (questServiceInstance != null) return questServiceInstance;
        else questServiceInstance = new QuestService();
        return questServiceInstance;
    }

    public List<Quest> getAllQuest(){
        return this.questDao.getObjects("name", "B%");
    }

}
