package pl.przemek.repository.inMemoryRepository;


import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.model.VoteComment;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaVoteRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JpaVoteCommentRepositoryInMemoryImpl implements JpaVoteRepository<VoteComment> {

    private List<VoteComment> listofVoteComments;

    public JpaVoteCommentRepositoryInMemoryImpl(){
        listofVoteComments=new ArrayList<>();
        populateVoteCommentList();
    }
    @Override
    public void add(VoteComment vote) {
        listofVoteComments.add(vote);
    }

    @Override
    public VoteComment update(VoteComment vote) {
        VoteComment voteComment;
        for(int i=0;i<listofVoteComments.size();i++){
            voteComment=listofVoteComments.get(i);
            if(voteComment.getId()==vote.getId())
                listofVoteComments.set(i,vote);
        }
        return vote;
    }

    @Override
    public void remove(VoteComment vote) {
        VoteComment voteComment;
        for(int i=0;i<listofVoteComments.size();i++){
            voteComment=listofVoteComments.get(i);
            if(voteComment.getId()==vote.getId())
                listofVoteComments.remove(voteComment);
        }
    }

    @Override
    public VoteComment get(Class<VoteComment> clazz, long id) {
        for (VoteComment voteComment : listofVoteComments) {
            if (voteComment.getId() == id)
                return voteComment;
        }
        return null;
    }

    @Override
    public List<VoteComment> getAll(String nameOfQuery, Class<VoteComment> clazz) {
        return null;
    }

    @Override
    public List<VoteComment> getVoteByUserIdVotedElementId(long userId, long commentId) {
        List<VoteComment> temporaryList=new ArrayList<>();
        for(VoteComment voteComment: listofVoteComments){
            if(voteComment.getUser().getId()==userId && voteComment.getComment().getId()==commentId)
                temporaryList.add(voteComment);
        }
        return temporaryList;
    }

    @Override
    public void removeByVotedElementId(long commentId) {
        VoteComment voteComment;
        for(int i=0;i<listofVoteComments.size();i++){
            voteComment=listofVoteComments.get(i);
            if(voteComment.getId()==commentId)
                listofVoteComments.remove(voteComment);
        }
    }
    private void populateVoteCommentList(){
        VoteComment voteComment;
        for(int i=0;i<10;i++){
            voteComment=new VoteComment();
            voteComment.setId(i);
            voteComment.setComment(new Comment());
            voteComment.setDate(new Timestamp(new Date().getTime()));
            voteComment.setUser(new User());
            if(i%2==0)
                voteComment.setVoteType(VoteType.VOTE_UP);
            else voteComment.setVoteType(VoteType.VOTE_DOWN);
            listofVoteComments.add(voteComment);
        }
    }
}
