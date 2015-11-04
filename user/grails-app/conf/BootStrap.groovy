import de.httc.plugins.user.User
import de.httc.plugins.user.Role
import de.httc.plugins.user.UserRole

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
        }
        environments {
            development {
                def testUser = new User(username:"test", password:"test", email:"stephan.tittel@httc.de", profile:[firstName:"User", lastName:"Test"]).save(flush: true)
                assert User.count() == 2
            }
        }
    }
    def destroy = {
    }
}
