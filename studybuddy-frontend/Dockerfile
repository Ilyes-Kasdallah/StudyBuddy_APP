# Step 1: Use a Node.js base image
FROM node:18-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app/

# Step 3: Copy application files into the container
COPY public/ /app/public
COPY src/ /app/src
COPY package.json /app/

# Step 4: Install dependencies
RUN npm install

# Step 5: Specify the command to run the application
CMD ["npm", "start"]
