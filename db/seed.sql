INSERT INTO user (name, created)
  VALUES
    ('test1', '2007-01-01'),
    ('test2', '2007-01-01'),
    ('test3', '2007-01-01');

INSERT INTO diary (user_id, title, created)
  VALUES
    (1, 'diary1', '2007-01-01'),
    (1, 'diary2', '2007-01-01'),
    (2, 'diary3', '2007-01-01');

INSERT INTO article (diary_id, title, body, created, updated)
  VALUES
    (1, 'monster', 'hello monster', '2007-01-01', '2017-01-01'),
    (1, 'monster2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (2, 'monster3', 'hello monster3', '2009-01-10', '2017-01-01'),
    (3, 'monster22312', 'hello monster33', '2010-01-10', '2017-01-01'),
    (1, 'monster123', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'monsteasdfar2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons123ter22', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'monstwer2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'monster22312stwer2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons1t er2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons1231123ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons3ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons4ter2', 'hello monster222', '2008-01-10', '2017-01-01'),
    (1, 'mons5ter2', 'hello monster222', '2008-01-10', '2017-01-01');
