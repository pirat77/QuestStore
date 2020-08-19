package service;

import DAO.ArtifactDAO;
import model.elements.Artifact;

import java.util.List;

public class ArtifactService {
    ArtifactDAO artifactDAO;
    static ArtifactService artifactServiceInstance;

    ArtifactService(){
        this.artifactDAO = new ArtifactDAO();
    }

    static public ArtifactService getInstance(){
        if (artifactServiceInstance != null) return artifactServiceInstance;
        else artifactServiceInstance = new ArtifactService();
        return artifactServiceInstance;
    }

    public List<Artifact> getAllArtifact(){
        return this.artifactDAO.getObjects("id", "%");
    }

}