import qrcode

def generate_qr_code(content, filename):
    """
    Generates a QR code with the given content and saves it as an image file.

    Args:
        content (str): The content to be encoded in the QR code.
        filename (str): The filename (including path) where the QR code image will be saved.
    """
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(content)
    qr.make(fit=True)

    img = qr.make_image(fill='black', back_color='white')
    img.save(filename)
    print(f"QR code generated and saved as {filename}")

# Example usage
if __name__ == "__main__":
    slots = [
        {"slot_id": "slot_001", "QR_code": "QR001"},
        {"slot_id": "slot_002", "QR_code": "QR002"},
        # Add more slots as needed
    ]

    for slot in slots:
        content = f"slot_id:{slot['slot_id']},QR_code:{slot['QR_code']}"
        filename = f"{slot['slot_id']}.png"
        generate_qr_code(content, filename)
