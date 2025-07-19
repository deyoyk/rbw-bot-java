package com.deyo.rbw.features;



import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

/**
 * Generic confirmation listener.
 *
 * A command sends an embed with two buttons carrying a custom ID in the format:
 *   confirm:<actionId>:<targetId>:<authorId>
 *   cancel :<actionId>:<targetId>:<authorId>
 *
 * The authorId is used to ensure only the staff member that initiated the action can confirm/cancel.
 * Once a decision is made, the buttons are disabled and the event is acknowledged.
 */
public class ConfirmHandler extends ListenerAdapter {

    public interface ConfirmAction {
        void execute(String targetId);
    }

    private final ConfirmAction onConfirm;
    private final String authorId;

    public ConfirmHandler(ConfirmAction action, String authorId) {
        this.onConfirm = action;
        this.authorId = authorId;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");
        if (parts.length < 4) return;
        if (!parts[2].equals(authorId)) return; // not initiator

        String decision = parts[0];
        String targetId = parts[1];

        if (decision.equals("confirm")) {
            onConfirm.execute(targetId);
            event.editMessage("Action confirmed.").setComponents().queue();
        } else if (decision.equals("cancel")) {
            event.editMessage("Action cancelled.").setComponents().queue();
        }
        // remove listener after first use
        event.getJDA().removeEventListener(this);
    }

    /**
     * Utility to build the confirm / cancel button row.
     */
    public static Button[] buildButtons(String targetId, User staff) {
        String authorId = staff.getId();
        return new Button[]{
                Button.success("confirm:" + targetId + ":" + authorId, "Confirm"),
                Button.danger("cancel:" + targetId + ":" + authorId, "Cancel")
        };
    }
}
