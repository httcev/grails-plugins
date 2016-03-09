
class BootStrap {

    def init = { servletContext ->
        environments {
            development {
            }
        }
    }
    def destroy = {
    }
}
