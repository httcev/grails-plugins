import de.httc.plugins.user.User
import de.httc.plugins.qaa.Question
import de.httc.plugins.qaa.Answer
import de.httc.plugins.qaa.Comment

class BootStrap {
    def init = { servletContext ->
        environments {
            development {
                if (Question.count() == 0) {
                    def testUser = new User(username:"tittel", password:"tittel", email:"stephan.tittel@kom.tu-darmstadt.de", profile:[firstName:"Stephan", lastName:"Tittel", company:"httc e.V.", phone:"+49615116882", mobile:"+4915114474556"]).save(flush: true)

                    def numQuestions = 2
                    for (int i=0; i<numQuestions; i++) {
                        def question = new Question(title:"Ich habe eine Frage $i", text:"Was ist grün und hüpft von Baum zu Baum?", creator:testUser)
                        def answer = new Answer(text:"Weiss nicht! Ein Frosch?", creator:testUser)
                        answer.addToComments(new Comment(text:"Antwortkommentar...", creator:testUser))
                        question.addToAnswers(answer)
                        question.addToComments(new Comment(text:"Fragenkommentar 1...", creator:testUser))
                        question.addToComments(new Comment(text:"Fragenkommentar 2...", creator:testUser))
                        if (!question.save(true)) {
                            question.errors.allErrors.each { println it }
                        }
                    }
                    assert Question.count() == numQuestions
                    assert Answer.count() == numQuestions
                    assert Comment.count() == numQuestions * 3
                }
            }
        }
    }
    def destroy = {
    }
}
