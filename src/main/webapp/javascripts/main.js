(function() {
  class View {
    constructor(element) {
      this.container = element;

      this.logoutButton = this.container.querySelector(".logout");

      this.logoutButton.addEventListener('click', (e) => {
        const logoutEvent = new CustomEvent('logout', {});
        document.dispatchEvent(logoutEvent);
      });
    }
  }

  class Controller {
    constructor(view) {
      this.view = view;

      document.addEventListener('logout', async (e) => {
        try {
          const res = await this.logout();
          console.log(res);
          console.log(document.cookie);
        } catch(error) {
          console.error(error);
        }
      });
    }

    logout() {
      return fetch('/logout', {
        method: 'POST'
      });
    }
  }

  new Controller(
    new View(document.querySelector('#content'))
  );
})()
