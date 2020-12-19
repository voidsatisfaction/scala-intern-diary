(function() {
  class View {
    constructor(element) {
      this.container = element;

      this.logoutButton = this.container.querySelector(".logout");
      this.articleContainer = this.container.querySelector(".article-container");

      if(this.logoutButton) {
        this.logoutButton.addEventListener('click', (e) => {
          const logoutEvent = new CustomEvent('logout', {});
          console.log(logoutEvent);
          document.dispatchEvent(logoutEvent);
        });
      }

      if(this.articleContainer) {
        this.articleContainer.addEventListener('click', (e) => {
          const articleId = e.toElement.dataset.articleId;
          const deleteArticleEvent = new CustomEvent('deleteArticle', {
            detail: { articleId }
          });
          document.dispatchEvent(deleteArticleEvent);
        });
      }
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
        credentials: 'same-origin',
        method: 'POST'
      })
      .then(() => location.reload())
      .catch((error) => alert(error));
    }

    deleteArticle({ articleId }) {
      return fetch(`/articles/${articleId}`, {
        credentials: 'same-origin',
        method: 'DELETE'
      })
      .then(() => location.reload())
      .catch((error) => alert(error));
    }
  }

  new Controller(
    new View(document.querySelector('#content'))
  );
})()
