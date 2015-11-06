import de.httc.plugins.user.User
import de.httc.plugins.user.Role
import de.httc.plugins.user.UserRole
import de.httc.plugins.repository.Asset

class BootStrap {

    def init = { servletContext ->

        if (Role.count() == 0) {
            def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
            assert Role.count() == 1

            def adminUser = new User(username:"admin", password:"admin", email:"stephan.tittel@httc.de", profile:[firstName:"User", lastName:"Admin"])

            if (!adminUser.save(flush: true)) {
                adminUser.errors.allErrors.each {
                    println it
                }
            }
            assert User.count() == 1

            UserRole.create(adminUser, adminRole, true)
            assert UserRole.count() == 1

            def asset
        }
        environments {
            development {
                def testUser = new User(username:"test", password:"test", email:"stephan.tittel@httc.de", profile:[firstName:"User", lastName:"Test"]).save(flush: true)
                assert User.count() == 2

                def numMicrolearnings = 1
                for (i in 1..numMicrolearnings) {
                    def ml = new Asset(name:"Lernmodul "+i, mimeType:"text/html", props:[(Asset.PROP_TYPE):"microlearning", (Asset.PROP_EXTERNAL_URL):"https://www.youtube.com/embed/jEYaD9MbjVw","goal":"Verstehen", "topic" : "Das Lernthema ist..."])
                    if (!ml.save(flush:true)) {
                        ml.errors.allErrors.each {
                            println it
                        }
                    }
                }
                def asset = new Asset(name:"Fehlerbericht 1", mimeType:"text/plain", props:[(Asset.PROP_TYPE):"errorreport", (Asset.PROP_DESCRIPTION):"Beschreibung des Berichts","cause":"Ursache","solution":"Lösung..."])
                if (!asset.save(flush:true)) {
                    asset.errors.allErrors.each {
                        println it
                    }
                }
                new Asset(name:"Fehlerbericht 2", mimeType:"text/plain", props:[(Asset.PROP_TYPE):"errorreport", (Asset.PROP_DESCRIPTION):"Der Fehler tritt auf wenn","cause":"Ursachenforschung muss noch betrieben werden.","solution":"och keine Lösung in Sicht."]).save(flush:true)
                println "--- ASSETS CREATED."

                assert Asset.count() == 2 + numMicrolearnings
            }
        }
    }
    def destroy = {
    }
}
