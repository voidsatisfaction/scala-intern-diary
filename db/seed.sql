INSERT INTO user (user_id, name, created)
  VALUES
    (1, 'test1', '2007-01-01'),
    (2, 'test2', '2007-01-01'),
    (3, 'test3', '2007-01-01');

INSERT INTO diary (diary_id, user_id, title, created)
  VALUES
    (1, 1, 'diary1', '2007-01-01'),
    (2, 1, 'diary2', '2007-01-01'),
    (3, 2, 'diary3', '2007-01-01');

INSERT INTO article (article_id, diary_id, title, body, created, updated)
  VALUES
    (1, 1, 'monster', 'hello monster', '2007-01-01', '2017-01-01'),
    (2, 1, 'monster2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (3, 2, 'monster3', 'hello monster3', '2009-01-10', '2017-01-01'),
    (4, 3, 'monster22312', 'hello monster33', '2010-01-10', '2017-01-01'),
    (5, 1, 'monster123', 'hello monster222', '2008-01-10', '2017-01-01'),
    (6, 1, 'monsteasdfar2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (7, 1, 'mons123ter22', 'hello monster222', '2008-01-10', '2017-01-01'),
    (8, 1, 'monstwer2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (9, 1, 'monster22312stwer2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (10, 1, 'mons1t er2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (11, 1, 'mons1231123ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (12, 1, 'mons3ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (13, 1, 'mons4ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (14, 1, 'mons5ter2', 'hello monster222', '2008-01-10', '2017-01-01');
