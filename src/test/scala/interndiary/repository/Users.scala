package interndiary.repository

import interndiary.helper._

class UserRepositorySpec extends UnitSpec with SetupDB with Factory {
  describe("User Repository") {
    describe("create") {
      it("should be created with name") {
        val name = randomString(10)
        val createdUser = Users.create(name)
        val foundedUser = Users.findByName(createdUser.name)

        createdUser.name shouldBe foundedUser.getOrElse(null).name
      }
    }

    describe("findByName") {
      it("should return user") {
        val dUser = dummyUser

        val fetchedUser = Users.findByName(dUser.name).getOrElse(null)

        fetchedUser should not be null
        fetchedUser.name shouldBe dUser.name
      }
    }
  }
}
