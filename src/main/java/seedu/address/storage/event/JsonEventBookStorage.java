package seedu.address.storage.event;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyEventBook;

/**
 * A class to access EventBook data stored as a json file on the hard disk.
 */
public class JsonEventBookStorage implements EventBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonEventBookStorage.class);

    private Path filePath;

    public JsonEventBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getEventBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyEventBook> readEventBook() throws DataLoadingException {
        return readEventBook(filePath);
    }

    /**
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyEventBook> readEventBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableEventBook> jsonEventBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableEventBook.class);
        if (!jsonEventBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonEventBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveEventBook(ReadOnlyEventBook eventBook) throws IOException {
        saveEventBook(eventBook, filePath);
    }

    /**
     * @param filePath location of the data. Cannot be null.
     */
    public void saveEventBook(ReadOnlyEventBook eventBook, Path filePath) throws IOException {
        requireNonNull(eventBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableEventBook(eventBook), filePath);
    }

}
