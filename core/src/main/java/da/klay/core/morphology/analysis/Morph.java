package da.klay.core.morphology.analysis;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Morph {

    private CharSequence text;
    private CharSequence pos;

    public Morph(CharSequence text,
                 CharSequence pos) {
        this.text = text;
        this.pos = pos;
    }
}
