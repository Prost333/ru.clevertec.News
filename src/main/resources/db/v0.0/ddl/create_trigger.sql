CREATE OR REPLACE FUNCTION delete_comments_on_news_delete() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  IF TG_OP = 'DELETE' THEN
    DELETE FROM comments WHERE news_id = OLD.id;
    RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER delete_comments
BEFORE DELETE ON news
FOR EACH ROW
EXECUTE FUNCTION delete_comments_on_news_delete();