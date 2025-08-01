package dev.spiritstudios.abysm.client.gui.screen.ingame;

import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.networking.UpdateDensityBlobBlockC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class DensityBlobBlockScreen extends Screen {
	private static final Text FINAL_STATE_TEXT = Text.translatable("jigsaw_block.final_state");

	private final DensityBlobBlockEntity densityBlob;
	private TextFieldWidget finalStateField;
	private ButtonWidget doneButton;

	public DensityBlobBlockScreen(DensityBlobBlockEntity densityBlob) {
		super(NarratorManager.EMPTY);
		this.densityBlob = densityBlob;
	}

	private void onDone() {
		this.updateServer();
		Objects.requireNonNull(this.client);
		this.client.setScreen(null);
	}

	private void onCancel() {
		Objects.requireNonNull(this.client);
		this.client.setScreen(null);
	}

	private void updateServer() {
		ClientPlayNetworking.send(new UpdateDensityBlobBlockC2SPayload(
			this.densityBlob.getPos(),
			this.finalStateField.getText()
		));
	}

	@Override
	public void close() {
		this.onCancel();
	}

	@Override
	protected void init() {
		this.finalStateField = new TextFieldWidget(this.textRenderer, this.width / 2 - 153, 125, 300, 20, FINAL_STATE_TEXT);
		this.finalStateField.setMaxLength(256);
		this.finalStateField.setText(this.densityBlob.getFinalState());
		this.addSelectableChild(this.finalStateField);

		this.doneButton = this.addDrawableChild(
			ButtonWidget.builder(ScreenTexts.DONE, button -> this.onDone()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build()
		);

		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.onCancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

		this.updateDoneButtonState();
	}

	@Override
	protected void setInitialFocus() {
		this.setInitialFocus(this.finalStateField);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		this.renderInGameBackground(context);
	}

	private void updateDoneButtonState() {
		boolean active = true;
		this.doneButton.active = active;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String finalState = this.finalStateField.getText();
		this.init(client, width, height);
		this.finalStateField.setText(finalState);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (!this.doneButton.active || keyCode != GLFW.GLFW_KEY_ENTER && keyCode != GLFW.GLFW_KEY_KP_ENTER) {
			return false;
		} else {
			this.onDone();
			return true;
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);

		context.drawTextWithShadow(this.textRenderer, FINAL_STATE_TEXT, this.width / 2 - 153, 115, 10526880);
		this.finalStateField.render(context, mouseX, mouseY, deltaTicks);
	}
}
