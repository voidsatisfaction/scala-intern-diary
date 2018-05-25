(function() {
  class View {
    constructor(element) {
      this.container = element;

      this.logoutButton = this.container.querySelector(".logout");
      this.articleContainer = this.container.querySelector(".article-container");

      this.logoutButton.addEventListener('click', (e) => {
        const logoutEvent = new CustomEvent('logout', {});
        document.dispatchEvent(logoutEvent);
      });

      this.articleContainer.addEventListener('click', (e) => {
        const articleId = +e.toElement.dataset.articleId;
        const deleteArticleEvent = new CustomEvent('deleteArticle', {
          detail: { articleId }
        });
        document.dispatchEvent(deleteArticleEvent);
      });
    }
  }

  class Controller {
    constructor(view) {
      this.view = view;

      document.addEventListener('logout', async (e) => {
        try {
          const res = await this.logout();
        } catch(error) {
          console.error(error);
        }
      });

      document.addEventListener('deleteArticle', async (e) => {
        try {
          const articleId = e.detail.articleId;
          const res = await this.deleteArticle({ articleId });
          console.log(res);
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

    deleteArticle({ articleId }) {
      return fetch(`/articles/${articleId}`, {
        credentials: 'same-origin',
        method: 'DELETE'
      })
      .then(() => location.reload('/'))
      .catch((error) => alert(error));
    }
  }

  new Controller(
    new View(document.querySelector('#content'))
  );
})()
