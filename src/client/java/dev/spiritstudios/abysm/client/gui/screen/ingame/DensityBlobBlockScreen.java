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
	private static final Text BLOB_SAMPLER_IDENTIFIER_TEXT = Text.translatable("abysm.density_blob_block.blob_sampler_identifier");

	private final DensityBlobBlockEntity densityBlob;
	private TextFieldWidget finalStateField;
	private TextFieldWidget blobSamplerIdentifierField;
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
			this.finalStateField.getText(),
			this.blobSamplerIdentifierField.getText()
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

		this.blobSamplerIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 153, 160, 300, 20, BLOB_SAMPLER_IDENTIFIER_TEXT);
		this.blobSamplerIdentifierField.setMaxLength(256);
		this.blobSamplerIdentifierField.setText(this.densityBlob.getBlobsSamplerIdentifier());
		this.addSelectableChild(this.blobSamplerIdentifierField);

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
		String blobSamplerIdentifier = this.blobSamplerIdentifierField.getText();
		this.init(client, width, height);
		this.finalStateField.setText(finalState);
		this.blobSamplerIdentifierField.setText(blobSamplerIdentifier);
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

		context.drawTextWithShadow(this.textRenderer, BLOB_SAMPLER_IDENTIFIER_TEXT, this.width / 2 - 153, 150, 10526880);
		this.blobSamplerIdentifierField.render(context, mouseX, mouseY, deltaTicks);
	}
}
