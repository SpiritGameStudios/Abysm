package dev.spiritstudios.abysm.client.gui.screen.ingame;

import dev.spiritstudios.abysm.network.UpdateDensityBlobBlockC2SPayload;
import dev.spiritstudios.abysm.world.level.block.entity.DensityBlobBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class DensityBlobBlockScreen extends Screen {
	private static final Component FINAL_STATE_TEXT = Component.translatable("jigsaw_block.final_state");
	private static final Component BLOB_SAMPLER_IDENTIFIER_TEXT = Component.translatable("abysm.density_blob_block.blob_sampler_identifier");

	private final DensityBlobBlockEntity densityBlob;
	private EditBox finalStateField;
	private EditBox blobSamplerIdentifierField;
	private Button doneButton;

	public DensityBlobBlockScreen(DensityBlobBlockEntity densityBlob) {
		super(GameNarrator.NO_TITLE);
		this.densityBlob = densityBlob;
	}

	private void onDone() {
		this.updateServer();
		Objects.requireNonNull(this.minecraft);
		this.minecraft.setScreen(null);
	}

	private void onCancel() {
		Objects.requireNonNull(this.minecraft);
		this.minecraft.setScreen(null);
	}

	private void updateServer() {
		ClientPlayNetworking.send(new UpdateDensityBlobBlockC2SPayload(
			this.densityBlob.getBlockPos(),
			this.finalStateField.getValue(),
			this.blobSamplerIdentifierField.getValue()
		));
	}

	@Override
	public void onClose() {
		this.onCancel();
	}

	@Override
	protected void init() {
		this.finalStateField = new EditBox(this.font, this.width / 2 - 153, 125, 300, 20, FINAL_STATE_TEXT);
		this.finalStateField.setMaxLength(256);
		this.finalStateField.setValue(this.densityBlob.getFinalState());
		this.addWidget(this.finalStateField);

		this.blobSamplerIdentifierField = new EditBox(this.font, this.width / 2 - 153, 160, 300, 20, BLOB_SAMPLER_IDENTIFIER_TEXT);
		this.blobSamplerIdentifierField.setMaxLength(256);
		this.blobSamplerIdentifierField.setValue(this.densityBlob.getBlobsSamplerIdentifier());
		this.addWidget(this.blobSamplerIdentifierField);

		this.doneButton = this.addRenderableWidget(
			Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).bounds(this.width / 2 - 4 - 150, 210, 150, 20).build()
		);

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onCancel()).bounds(this.width / 2 + 4, 210, 150, 20).build());

		this.updateDoneButtonState();
	}

	@Override
	protected void setInitialFocus() {
		this.setInitialFocus(this.finalStateField);
	}

	@Override
	public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
		this.renderTransparentBackground(context);
	}

	private void updateDoneButtonState() {
		boolean active = true;
		this.doneButton.active = active;
	}


	@Override
	public void resize(int width, int height) {
		String finalState = this.finalStateField.getValue();
		String blobSamplerIdentifier = this.blobSamplerIdentifierField.getValue();
		this.init(width, height);
		this.finalStateField.setValue(finalState);
		this.blobSamplerIdentifierField.setValue(blobSamplerIdentifier);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (super.keyPressed(event)) {
			return true;
		} else if (!this.doneButton.active || event.key() != GLFW.GLFW_KEY_ENTER && event.key() != GLFW.GLFW_KEY_KP_ENTER) {
			return false;
		} else {
			this.onDone();
			return true;
		}
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);

		context.drawString(this.font, FINAL_STATE_TEXT, this.width / 2 - 153, 115, 0xa0a0a0);
		this.finalStateField.render(context, mouseX, mouseY, deltaTicks);

		context.drawString(this.font, BLOB_SAMPLER_IDENTIFIER_TEXT, this.width / 2 - 153, 150, 0xa0a0a0);
		this.blobSamplerIdentifierField.render(context, mouseX, mouseY, deltaTicks);
	}
}
