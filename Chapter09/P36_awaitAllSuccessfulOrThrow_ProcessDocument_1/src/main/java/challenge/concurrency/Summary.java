package challenge.concurrency;

import java.util.List;

public record Summary(boolean formatted, List<Spelling> spellings, Validator valid) {}
