module SlickTweet
  
  class WelcomeScreenView < View
    
    def render
      # authorize only logged in users to use the app
      if $current_user.nil?
        puts "You need to login to continue"
        $current_screen = :login
        return
      end
      print options
      choice = gets.chomp
      handle_choice choice
    end

    # view
    def options
      system "clear"
      options = "Welcome #{$current_user.username}\n"
      options_heading = '-' * options.length << "\n"
      options.prepend options_heading
      options << options_heading
      options << "What do you want to do today?\n"
      options << "1. Tweet\n"
      options << "2. Search\n"
      options << "3. Your Tweets\n"
      options << "4. Timeline\n"
      options << "5. Followers\n"
      options << "6. Following\n"
      options << "7. Edit Profile\n"
      options << "8. Logout\n"
      options << "Choose: "
      options
    end

    def handle_choice choice
      case choice
      when "1"
        # render sign up view
        $current_screen = :tweet
      when "2"
        # render login view
        $current_screen = :search
      when "3"
        $current_screen = :your_tweets
      when "4"
        $current_screen = :timeline
      when "5"
        $current_screen = :followers
      when "6"
        $current_screen = :following
      when "7"
        $current_screen = :edit_profile
      when "8"
        $current_screen = :logout
      else
        puts "Wrong choice"
        choice = gets.chomp
        handle_choice choice
      end
    end

  end

end